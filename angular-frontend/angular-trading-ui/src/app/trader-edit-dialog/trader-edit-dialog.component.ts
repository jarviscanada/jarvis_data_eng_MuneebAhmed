import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-trader-edit-dialog',
  templateUrl: './trader-edit-dialog.component.html',
  styleUrls: ['./trader-edit-dialog.component.css']
})
export class TraderEditDialogComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<TraderEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit(): void {
    // Initialize form with data
    this.data.firstName = this.data.firstName || '';
    this.data.lastName = this.data.lastName || '';
    this.data.email = this.data.email || '';
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}