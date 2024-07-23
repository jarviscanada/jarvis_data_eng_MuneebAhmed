import { Injectable } from '@angular/core';
import { Trader } from './trader';
import { of, Observable, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class TraderListService {
  private traderListSubject: BehaviorSubject<Trader[]> = new BehaviorSubject<
    Trader[]
  >([
    {
      key: '1',
      id: 1,
      firstName: 'Mike',
      lastName: 'Spencer',
      dob: '1990-01-01',
      country: 'Canada',
      email: 'mike@test.com',
      amount: 0,
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
    },
    {
      key: '2',
      id: 2,
      firstName: 'Hellen',
      lastName: 'Miller',
      dob: '1990-01-01',
      country: 'Austria',
      email: 'hellen@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
    {
      key: '3',
      id: 3,
      firstName: 'Jack',
      lastName: 'Reed',
      dob: '1990-01-01',
      country: 'United Kingdom',
      email: 'jack@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
    {
      key: '4',
      id: 4,
      firstName: 'Robert',
      lastName: 'Howard',
      dob: '1990-01-01',
      country: 'Switzerland',
      email: 'robert@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
    {
      key: '5',
      id: 5,
      firstName: 'Dustin',
      lastName: 'Wise',
      dob: '1990-01-01',
      country: 'Russia',
      email: 'dustin@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
    {
      key: '6',
      id: 6,
      firstName: 'Sergio',
      lastName: 'Chung',
      dob: '1990-01-01',
      country: 'China',
      email: 'sergio@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
    {
      key: '7',
      id: 7,
      firstName: 'Magnolia',
      lastName: 'Cortez',
      dob: '1990-01-01',
      country: 'Malaysia',
      email: 'magnolia@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
    {
      key: '8',
      id: 8,
      firstName: 'Jeremy',
      lastName: 'Alvarez',
      dob: '1990-01-01',
      country: 'Mexico',
      email: 'jeremy@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
    {
      key: '9',
      id: 9,
      firstName: 'Valerie',
      lastName: 'Lee',
      dob: '1990-01-01',
      country: 'Turkey',
      email: 'valerie@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
    {
      key: '10',
      id: 10,
      firstName: 'Lydia',
      lastName: 'Zeena',
      dob: '1990-01-01',
      country: 'Morocco',
      email: 'hellen@test.com',
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
      amount: 0,
    },
  ]);

  private baseUrl: string = 'http://localhost:3000';

  constructor(private http: HttpClient) {}

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
    console.log('Adding trader:', trader);
    const currentList = this.traderListSubject.value;
    const newId = currentList.length
      ? Math.max(...currentList.map((t) => t.id)) + 1
      : 1;
    const newTrader = {
      ...trader,
      key: newId.toString(),
      id: newId,
      amount: 0,
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
    };
    this.traderListSubject.next([...currentList, newTrader]);
  }

  deleteTrader(id: number): void {
    const currentList = this.traderListSubject.value;
    const updatedList = currentList.filter((trader) => trader.id !== id);
    this.traderListSubject.next(updatedList);
  }

  depositFunds(traderId: number, amount: number): void {
    const currentList = this.traderListSubject.value;
    const trader = currentList.find((t) => t.id === traderId);
    if (trader) {
      trader.amount += amount;
      this.traderListSubject.next([...currentList]);
    }
  }

  withdrawFunds(traderId: number, amount: number): void {
    const currentList = this.traderListSubject.value;
    const trader = currentList.find((t) => t.id === traderId);
    if (trader) {
      trader.amount -= amount;
      this.traderListSubject.next([...currentList]);
    }
  }

  updateTrader(id: number, updatedTrader: Partial<Trader>): void {
    const traders = this.traderListSubject.getValue();
    const index = traders.findIndex(trader => trader.id === id);
    if (index !== -1) {
      traders[index] = { ...traders[index], ...updatedTrader };
      this.traderListSubject.next([...traders]); // Ensure a new array is emitted
    }
  }
}
