import { Injectable } from '@angular/core';
import { Trader } from './trader';
import { Observable, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class TraderListService {
  private traderListSubject: BehaviorSubject<Trader[]> = new BehaviorSubject<Trader[]>([]);
  private baseUrl: string = 'http://localhost:8080/api/traders';

  constructor(private http: HttpClient) {
    this.loadInitialData();
  }

  private loadInitialData(): void {
    this.http.get<Trader[]>(this.baseUrl).subscribe((traders) => {
      this.traderListSubject.next(traders);
    });
  }

  getDataSource(): Observable<Trader[]> {
    return this.traderListSubject.asObservable();
  }

  getColumns(): { key: string, displayName: string }[] {
    return [
      { key: 'firstName', displayName: 'First Name' },
      { key: 'lastName', displayName: 'Last Name' },
      { key: 'email', displayName: 'Email' },
      { key: 'dob', displayName: 'Date of Birth' },
      { key: 'country', displayName: 'Country' },
      { key: 'actions', displayName: 'Actions' }
    ];
  }

  addTrader(trader: Trader): void {
    this.http.post<Trader>(this.baseUrl, trader).pipe(
      tap((newTrader) => {
        const currentList = this.traderListSubject.value;
        this.traderListSubject.next([...currentList, newTrader]);
      })
    ).subscribe();
  }

  deleteTrader(id: number): void {
    this.http.delete(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        const currentList = this.traderListSubject.value;
        const updatedList = currentList.filter((trader) => trader.id !== id);
        this.traderListSubject.next(updatedList);
      })
    ).subscribe();
  }

    depositFunds(traderId: number, amount: number): void {
  const url = `${this.baseUrl}/${traderId}/deposit`;
  this.http.post<Trader>(url, { amount }).pipe(
    tap((updatedTrader) => {
      const currentList = this.traderListSubject.value;
      const index = currentList.findIndex((t) => t.id === traderId);
      if (index !== -1) {
        currentList[index] = updatedTrader;
        this.traderListSubject.next([...currentList]);
      }
    })
  ).subscribe();
}

withdrawFunds(traderId: number, amount: number): void {
  const url = `${this.baseUrl}/${traderId}/withdraw`;
  this.http.post<Trader>(url, { amount }).pipe(
    tap((updatedTrader) => {
      const currentList = this.traderListSubject.value;
      const index = currentList.findIndex((t) => t.id === traderId);
      if (index !== -1) {
        currentList[index] = updatedTrader;
        this.traderListSubject.next([...currentList]);
      }
    })
  ).subscribe();
}

  updateTrader(id: number, updatedTrader: Partial<Trader>): void {
    const currentList = this.traderListSubject.value;
    const index = currentList.findIndex(trader => trader.id === id);
    if (index !== -1) {
      const updated = { ...currentList[index], ...updatedTrader };
      this.http.put<Trader>(`${this.baseUrl}/${id}`, updated).pipe(
        tap(() => {
          currentList[index] = updated;
          this.traderListSubject.next([...currentList]);
        })
      ).subscribe();
    }
  }
}