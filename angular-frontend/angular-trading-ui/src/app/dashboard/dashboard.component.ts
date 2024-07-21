import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TraderFormDialogComponent } from '../trader-form-dialog/trader-form-dialog.component';
import { TraderListService } from '../trader-list.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(public dialog: MatDialog, private traderListService: TraderListService, private cdr: ChangeDetectorRef) {}
  
  openTraderFormDialog(): void {
    const dialogRef = this.dialog.open(TraderFormDialogComponent, {
      width: `500px`
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Dialog result:', result); // Add this line
      if (result) {
        this.traderListService.addTrader(result);
        this.traderListService.getDataSource().subscribe(data => {
          console.log('Updated trader list:', data); // Add this line
          this.cdr.detectChanges(); // Trigger change detection
        });
      }
    });
  }

  ngOnInit(): void {
  }
}