import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { TraderListService } from '../trader-list.service';
import { Trader } from '../trader';
import { faTrash, faInfoCircle, faEdit } from '@fortawesome/free-solid-svg-icons';
import { MatDialog } from '@angular/material/dialog';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { TraderEditDialogComponent } from '../trader-edit-dialog/trader-edit-dialog.component';

@Component({
  selector: 'app-trader-list',
  templateUrl: './trader-list.component.html',
  styleUrls: ['./trader-list.component.css'],
})
export class TraderListComponent implements OnInit, AfterViewInit {
  traderList: Trader[] = [];
  displayedColumns: { key: string, displayName: string }[] = [];
  columnKeys: string[] = [];
  dataSource = new MatTableDataSource<Trader>();
  faTrash = faTrash;
  faInfoCircle = faInfoCircle;
  faEdit = faEdit;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private _traderList: TraderListService,
    public dialog: MatDialog,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this._traderList.getDataSource().subscribe((data) => {
      this.traderList = data;
      this.dataSource.data = this.traderList;
      this.cdr.detectChanges();
    });
    this.displayedColumns = this._traderList.getColumns();
    this.columnKeys = this.displayedColumns.map(c => c.key);
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

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

  openEditDialog(trader: Trader): void {
    const dialogRef = this.dialog.open(TraderEditDialogComponent, {
      width: '500px',
      data: trader
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this._traderList.updateTrader(trader.id, result);
        this._traderList.getDataSource().subscribe(data => {
          this.traderList = data;
          this.dataSource.data = this.traderList;
          this.cdr.detectChanges(); 
        });
      }
    });
  }
}