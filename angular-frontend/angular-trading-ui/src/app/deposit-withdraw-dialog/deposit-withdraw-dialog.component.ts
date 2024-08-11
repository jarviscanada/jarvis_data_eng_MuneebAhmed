import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-deposit-withdraw-dialog',
  templateUrl: './deposit-withdraw-dialog.component.html',
  styleUrls: ['./deposit-withdraw-dialog.component.css'],
})
export class DepositWithdrawDialogComponent implements OnInit {
  amount: number = 0;

  constructor(
    public dialogRef: MatDialogRef<DepositWithdrawDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { type: string; traderId: number }
  ) {}

  ngOnInit(): void {}

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    this.dialogRef.close({ amount: this.amount });
  }
}