import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { QuotesService } from '../quotes.service';
import { Quote } from '../quote';

@Component({
  selector: 'app-quotes-list',
  templateUrl: './quotes-list.component.html',
  styleUrls: ['./quotes-list.component.css']
})
export class QuotesListComponent implements OnInit, AfterViewInit {
  quoteList: Quote[] = [];
  displayedColumns: { key: string, displayName: string }[] = [];
  columnKeys: string[] = [];
  dataSource = new MatTableDataSource<Quote>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private quotesService: QuotesService) { }

  ngOnInit(): void {
    this.quotesService.getQuoteList().subscribe(quotes => {
      this.quoteList = quotes;
      this.dataSource.data = this.quoteList;
    });
    this.displayedColumns = this.quotesService.getColumns();
    this.columnKeys = this.displayedColumns.map(c => c.key);
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}