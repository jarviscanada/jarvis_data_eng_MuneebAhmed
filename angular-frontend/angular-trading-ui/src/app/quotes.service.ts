import { Injectable } from '@angular/core';
import { Quote } from './quote';
import { Observable, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class QuotesService {
  private quoteListSubject = new BehaviorSubject<Quote[]>([]);
  private baseUrl: string = 'http://localhost:8080/api/quotes';

  constructor(private http: HttpClient) {
    this.loadInitialData();
  }

  private loadInitialData(): void {
    this.http.get<Quote[]>(`${this.baseUrl}/dailyList`).subscribe((quotes) => {
      this.quoteListSubject.next(quotes);
    });
  }

  getQuoteList(): Observable<Quote[]> {
    return this.quoteListSubject.asObservable();
  }

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

  addQuote(quote: Quote): void {
    this.http.post<Quote>(this.baseUrl, quote).pipe(
      tap((newQuote) => {
        const currentList = this.quoteListSubject.value;
        this.quoteListSubject.next([...currentList, newQuote]);
      })
    ).subscribe();
  }
}