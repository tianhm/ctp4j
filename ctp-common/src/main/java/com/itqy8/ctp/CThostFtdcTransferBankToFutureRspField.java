/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.itqy8.ctp;

public class CThostFtdcTransferBankToFutureRspField {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected CThostFtdcTransferBankToFutureRspField(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CThostFtdcTransferBankToFutureRspField obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        ctpJNI.delete_CThostFtdcTransferBankToFutureRspField(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setRetCode(String value) {
    ctpJNI.CThostFtdcTransferBankToFutureRspField_RetCode_set(swigCPtr, this, value);
  }

  public String getRetCode() {
    return ctpJNI.CThostFtdcTransferBankToFutureRspField_RetCode_get(swigCPtr, this);
  }

  public void setRetInfo(String value) {
    ctpJNI.CThostFtdcTransferBankToFutureRspField_RetInfo_set(swigCPtr, this, value);
  }

  public String getRetInfo() {
    return ctpJNI.CThostFtdcTransferBankToFutureRspField_RetInfo_get(swigCPtr, this);
  }

  public void setFutureAccount(String value) {
    ctpJNI.CThostFtdcTransferBankToFutureRspField_FutureAccount_set(swigCPtr, this, value);
  }

  public String getFutureAccount() {
    return ctpJNI.CThostFtdcTransferBankToFutureRspField_FutureAccount_get(swigCPtr, this);
  }

  public void setTradeAmt(double value) {
    ctpJNI.CThostFtdcTransferBankToFutureRspField_TradeAmt_set(swigCPtr, this, value);
  }

  public double getTradeAmt() {
    return ctpJNI.CThostFtdcTransferBankToFutureRspField_TradeAmt_get(swigCPtr, this);
  }

  public void setCustFee(double value) {
    ctpJNI.CThostFtdcTransferBankToFutureRspField_CustFee_set(swigCPtr, this, value);
  }

  public double getCustFee() {
    return ctpJNI.CThostFtdcTransferBankToFutureRspField_CustFee_get(swigCPtr, this);
  }

  public void setCurrencyCode(String value) {
    ctpJNI.CThostFtdcTransferBankToFutureRspField_CurrencyCode_set(swigCPtr, this, value);
  }

  public String getCurrencyCode() {
    return ctpJNI.CThostFtdcTransferBankToFutureRspField_CurrencyCode_get(swigCPtr, this);
  }

  public CThostFtdcTransferBankToFutureRspField() {
    this(ctpJNI.new_CThostFtdcTransferBankToFutureRspField(), true);
  }

}