import { Injectable } from '@angular/core';
import { Quote } from './quote';
import { Observable, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class QuotesService {

  private quoteListSubject = new BehaviorSubject<Quote[]>([
    {
      "ticker": "FB",
      "lastPrice": 319.48,
      "bidPrice": 0,
      "bidSize": 13,
      "askPrice": 13,
      "askSize": 400
  },
  {
      "ticker": "AAPL",
      "lastPrice": 500.23,
      "bidPrice": 0,
      "bidSize": 18,
      "askPrice": 18,
      "askSize": 100
  },
  {
      "ticker": "MSFT",
      "lastPrice": 100.53,
      "bidPrice": 0,
      "bidSize": 25,
      "askPrice": 25,
      "askSize": 200
  },
  {
      "ticker": "GOOGL",
      "lastPrice": 500.99,
      "bidPrice": 0,
      "bidSize": 30,
      "askPrice": 10,
      "askSize": 400
  },
  {
      "ticker": "AMZN",
      "lastPrice": 85.50,
      "bidPrice": 0,
      "bidSize": 16,
      "askPrice": 16,
      "askSize": 150
  }
]);

getQuoteList(): Observable<Quote[]> {
  return this.quoteListSubject.asObservable();
}

  constructor(private _http: HttpClient) { }

  getColumns(): { key: string, displayName: string }[] {
    return [
      { key: 'ticker', displayName: 'Ticker' },
      { key: 'lastPrice', displayName: 'Last Price' },
      { key: 'bidPrice', displayName: 'Bid Price' },
      { key: 'bidSize', displayName: 'Bid Size' },
      { key: 'askPrice', displayName: 'Ask Price' },
      { key: 'askSize', displayName: 'Ask Size' }
    ];
  }
}