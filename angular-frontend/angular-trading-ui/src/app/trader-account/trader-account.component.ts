import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TraderListService } from '../trader-list.service';
import { Trader } from '../trader';
import { MatDialog } from '@angular/material/dialog';
import { DepositWithdrawDialogComponent } from '../deposit-withdraw-dialog/deposit-withdraw-dialog.component';

@Component({
  selector: 'app-trader-account',
  templateUrl: './trader-account.component.html',
  styleUrls: ['./trader-account.component.css']
})
export class TraderAccountComponent implements OnInit {
  trader: Trader | undefined;

  constructor(
    private route: ActivatedRoute,
    private traderListService: TraderListService,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.traderListService.getDataSource().subscribe((data) => {
      this.trader = data.find(trader => trader.id === id);
    });
  }

  openDepositDialog(): void {
    const dialogRef = this.dialog.open(DepositWithdrawDialogComponent, {
      width: '250px',
      data: { type: 'deposit', traderId: this.trader?.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.traderListService.depositFunds(this.trader?.id!, result.amount);
      }
    });
  }

  openWithdrawDialog(): void {
    const dialogRef = this.dialog.open(DepositWithdrawDialogComponent, {
      width: '250px',
      data: { type: 'withdraw', traderId: this.trader?.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.traderListService.withdrawFunds(this.trader?.id!, result.amount);
      }
    });
  }
}