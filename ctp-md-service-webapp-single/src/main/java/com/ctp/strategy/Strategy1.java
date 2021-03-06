package com.ctp.strategy;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

import com.ctp.data.entity.OHLCData15Minute;
import com.ctp.data.entity.OHLCData1Minute;
import com.ctp.data.service.MarketDataService;
import com.ctp.data.service.OHLCDataService;
import com.ctp.md.vo.PositionInfoVO;
import com.ctp.ta4j.strategy.RSI2Strategy;
import com.ctp.trader.dto.InvestorPositionDTO;
import com.ctp.trader.dto.TradingAccountDTO;
import com.ctp.trader.service.InvestorPositionService;
import com.ctp.trader.service.TraderService;
import com.ctp.trader.service.TradingAccountService;
import com.itqy8.ctp.CThostFtdcDepthMarketDataField;
import com.itqy8.framework.util.SpringPropertyResourceReader;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.TradingRecord;
import eu.verdelhan.ta4j.analysis.criteria.TotalProfitCriterion;

public class Strategy1 implements MarketTradeTrategy{

	private MarketDataService marketDataService;
	private OHLCDataService oHLCDataService;
	private TradingAccountService tradingAccountService;
	private TraderService traderService;
	private InvestorPositionService investorPositionService;
	private TradingAccountDTO taDto = new TradingAccountDTO();
	private PositionInfoVO piv = new PositionInfoVO();
	private TradingRecord tr = new TradingRecord();
	
	/**
	 * 每天程序启动的时候执行
	 * 
	 * @author meixinbin 2016-6-21 上午8:47:07
	 */
	public void init(){
		String brokerID = SpringPropertyResourceReader.getProperty("ctp.brokerId");
		String investorID = SpringPropertyResourceReader.getProperty("ctp.userid");
		String instrumentID = "rb1610";
		List<InvestorPositionDTO> ls = this.investorPositionService.getList(brokerID, instrumentID, investorID);
		if(ls!=null){
			for(InvestorPositionDTO dto:ls){
				if(dto.getPosiDirection()=='2'){
					piv.setBK(piv.getYDBK()+dto.getPosition());
				}else if(dto.getPosiDirection()=='3'){
					piv.setYDSK(piv.getYDSK()+dto.getPosition());
				}
			}
		}
		
		taDto = tradingAccountService.getTradingAccount(brokerID,investorID);
	}
	@Override
	public void trade(CThostFtdcDepthMarketDataField pDepthMarketData) {
		long start = System.currentTimeMillis();
//		MarketData md = new MarketData();
//		try {
//			BeanUtils.copyProperties(md, pDepthMarketData);
//			md.setId(new SimpleDateFormat("yyyyMMddHH:mm:ss:SSS").parse(md.getTradingDay()+md.getUpdateTime()+":"+md.getUpdateMillisec()).getTime());
//		} catch (IllegalAccessException | InvocationTargetException | ParseException e) {
//			e.printStackTrace();
//		}
//		this.marketDataService.save(md);
//		this.oHLCDataService.addOHLCData(md);
//        this.oHLCDataService.add(OHLCData1Minute.class, md.getInstrumentID(), md.getId());
//        this.oHLCDataService.add(OHLCData5Minute.class, md.getInstrumentID(), md.getId());
//        this.oHLCDataService.add(OHLCData10Minute.class, md.getInstrumentID(), md.getId());
//        this.oHLCDataService.add(OHLCData15Minute.class, md.getInstrumentID(), md.getId());
//        this.oHLCDataService.add(OHLCData30Minute.class, md.getInstrumentID(), md.getId());
//        this.oHLCDataService.add(OHLCData1Hour.class, md.getInstrumentID(), md.getId());
//	    this.oHLCDataService.add(OHLCData2Hour.class, md.getInstrumentID(), md.getId());
//        this.oHLCDataService.add(OHLCData1Day.class, md.getInstrumentID(), md.getId());
		TimeSeries ts=new TimeSeries("rb1610",Period.minutes(1));
		//
		List<OHLCData1Minute> ls = this.oHLCDataService.getList(OHLCData1Minute.class, pDepthMarketData.getInstrumentID(), 300);
		for(OHLCData1Minute o:ls){
			ts.addTick(new Tick(new DateTime(o.getId()), Decimal.valueOf(o.getOpenPrice()), Decimal.valueOf(o.getHighPrice()), Decimal.valueOf(o.getLowPrice()), Decimal.valueOf(o.getClosePrice()),Decimal.valueOf(o.getVolume())));
		}
		Strategy strategy = RSI2Strategy.buildStrategy(ts);
		if(strategy.shouldEnter(ts.getEnd())){
			if(piv.getBK()==0 && piv.getYDBK()==0){
				this.traderService.bk(pDepthMarketData.getInstrumentID(), pDepthMarketData.getExchangeID(), pDepthMarketData.getAskPrice1(), '1', 1);
				this.piv.setBK(1);
				tr.enter(ts.getEnd(), Decimal.valueOf(pDepthMarketData.getAskPrice1()), Decimal.valueOf(1));
			}
			if(this.piv.getSK()>0){
				this.traderService.bp(pDepthMarketData.getInstrumentID(), pDepthMarketData.getExchangeID(), pDepthMarketData.getAskPrice1(), '1', 1);
				this.piv.setSK(0);
			}
		}
		
		if(strategy.shouldExit(ts.getEnd())){
			if(piv.getYDBK()>0 || piv.getBK()>0){
				//平仓
				this.traderService.sp(pDepthMarketData.getInstrumentID(), pDepthMarketData.getExchangeID(), pDepthMarketData.getBidPrice1(), '1', piv.getYDSK()+piv.getSK());
				this.piv.setSK(0);
				this.piv.setYDSK(0);
			}else{
				this.traderService.sk(pDepthMarketData.getInstrumentID(), pDepthMarketData.getExchangeID(), pDepthMarketData.getAskPrice1(), '1',1);
				this.piv.setSK(1);
			}
		}
		System.out.println("策略计算用时："+(System.currentTimeMillis()-start) +",tick:"+ts.getTickCount()+",trades:"+tr.getTradeCount()+",profit:"+new TotalProfitCriterion().calculate(ts, tr));
	}
	public void setTraderService(TraderService traderService) {
		this.traderService = traderService;
	}
	public void setoHLCDataService(OHLCDataService oHLCDataService) {
		this.oHLCDataService = oHLCDataService;
	}
	public void setTradingAccountService(TradingAccountService tradingAccountService) {
		this.tradingAccountService = tradingAccountService;
	}
	public void setMarketDataService(MarketDataService marketDataService) {
		this.marketDataService = marketDataService;
	}
	public void setInvestorPositionService(InvestorPositionService investorPositionService) {
		this.investorPositionService = investorPositionService;
	}
}
