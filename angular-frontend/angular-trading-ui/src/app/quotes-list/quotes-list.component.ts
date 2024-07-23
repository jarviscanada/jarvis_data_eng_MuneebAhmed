import { Component, OnInit } from '@angular/core';
import { QuotesService } from '../quotes.service';
import { Quote } from '../quote';

@Component({
  selector: 'app-quotes-list',
  templateUrl: './quotes-list.component.html',
  styleUrls: ['./quotes-list.component.css']
})
export class QuotesListComponent implements OnInit {
  quoteList: Quote[] = [];
  displayedColumns: { key: string, displayName: string }[] = [];
  columnKeys: string[] = [];

  constructor(private quotesService: QuotesService) { }

  ngOnInit(): void {
    this.quotesService.getQuoteList().subscribe(quotes => {
      this.quoteList = quotes;
    });
    this.displayedColumns = this.quotesService.getColumns();
    this.columnKeys = this.displayedColumns.map(c => c.key);
  }
}