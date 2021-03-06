/**
 * Copyright (C) 2012 - 2014 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.campbx.service.polling;

import java.io.IOException;
import java.util.List;

import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.campbx.CampBX;
import com.xeiam.xchange.campbx.CampBXUtils;
import com.xeiam.xchange.campbx.dto.marketdata.CampBXOrderBook;
import com.xeiam.xchange.campbx.dto.marketdata.CampBXTicker;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.ExchangeInfo;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;
import com.xeiam.xchange.utils.Assert;

/**
 * @author Matija Mazi
 */
public class CampBXMarketDataServiceRaw extends BasePollingExchangeService {

  private final CampBX campBX;

  /**
   * Constructor
   * 
   * @param exchangeSpecification The {@link ExchangeSpecification}
   */
  public CampBXMarketDataServiceRaw(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);
    this.campBX = RestProxyFactory.createProxy(CampBX.class, exchangeSpecification.getSslUri());
  }

  public CampBXTicker getCampBXTicker(String tradableIdentifier, String currency) throws IOException {

    verify(tradableIdentifier, currency);

    CampBXTicker campbxTicker = campBX.getTicker();

    if (!campbxTicker.isError()) {
      return campbxTicker;
    }
    else {
      throw new ExchangeException("Error calling getCampBXTicker(): " + campbxTicker.getError());
    }
  }

  public CampBXOrderBook getCampBXOrderBook(String tradableIdentifier, String currency) throws IOException {

    verify(tradableIdentifier, currency);

    CampBXOrderBook campBXOrderBook = campBX.getOrderBook();

    if (!campBXOrderBook.isError()) {
      return campBXOrderBook;
    }
    else {
      throw new ExchangeException("Error calling getCampBXFullOrderBook(): " + campBXOrderBook.getError());
    }
  }

  public Trades getCampBXTrades(String tradableIdentifier, String currency, Object... args) throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  public ExchangeInfo getCampBXExchangeInfo() throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  /**
   * Verify
   * 
   * @param tradableIdentifier The tradeable identifier (e.g. BTC in BTC/USD)
   * @param currency
   */
  private void verify(String tradableIdentifier, String currency) {

    Assert.notNull(tradableIdentifier, "tradableIdentifier cannot be null");
    Assert.notNull(currency, "currency cannot be null");
    Assert.isTrue(CampBXUtils.isValidCurrencyPair(new CurrencyPair(tradableIdentifier, currency)), "currencyPair is not valid:" + tradableIdentifier + " " + currency);
  }

  public List<CurrencyPair> getCampBXExchangeSymbols() {

    return CampBXUtils.CURRENCY_PAIRS;
  }
}
