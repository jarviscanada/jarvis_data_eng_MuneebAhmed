import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-trader-form-dialog',
  templateUrl: './trader-form-dialog.component.html',
  styleUrls: ['./trader-form-dialog.component.css']
})
export class TraderFormDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<TraderFormDialogComponent>) { }

  ngOnInit(): void {
  }

  onCancel(): void {
    this.dialogRef.close();
  }

}
