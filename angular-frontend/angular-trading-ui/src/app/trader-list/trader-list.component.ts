import { Component, OnInit } from '@angular/core';
import { TraderListService } from '../trader-list.service';
import { Trader } from '../trader';
import { faTrash, faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import { TraderFormDialogComponent } from '../trader-form-dialog/trader-form-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-trader-list',
  templateUrl: './trader-list.component.html',
  styleUrls: ['./trader-list.component.css'],
})
export class TraderListComponent implements OnInit {
  traderList: Trader[] = [];
  displayedColumns: string[] = [
    'id',
    'firstName',
    'lastName',
    'dob',
    'country',
    'email',
    'amount',
    'actions',
  ];
  faTrash = faTrash;
  faInfoCircle = faInfoCircle;

  constructor(
    private _traderList: TraderListService,
    public dialog: MatDialog,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this._traderList.getDataSource().subscribe((data) => {
      this.traderList = data;
      this.cdr.detectChanges();
    });
    this.displayedColumns = this._traderList.getColumns();
  }

  // openAddTraderDialog(): void {
  //   const dialogRef = this.dialog.open(TraderFormDialogComponent, {
  //     width: '250px',
  //   });

  //   dialogRef.afterClosed().subscribe((result) => {
  //     console.log('Dialog result:', result);
  //     if (result) {
  //       this._traderList.addTrader(result);
  //     }
  //   });
  // }
  viewTraderDetails(id: number): void {
    this.router.navigate(['/trader-account', id]);
  }

  deleteTrader(event: Event, id: number): void {
    try {
      this._traderList.deleteTrader(id);
    } catch (err) {
      console.log(err);
    }
  }
}
