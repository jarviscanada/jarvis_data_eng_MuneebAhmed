import { Component, OnInit } from '@angular/core';
import { ToDoService } from '../todo.service';

@Component({
  selector: 'app-to-do-page',
  templateUrl: './to-do-page.component.html',
  styleUrls: ['./to-do-page.component.css']
})
export class ToDoPageComponent implements OnInit {
  newToDo: string = '';
  toDoList: string[] = [];
  editIndex: number[] = [];
  editTodo: string = '';

  constructor(private toDoService: ToDoService) { }

  ngOnInit(): void {
    this.toDoList = this.toDoService.getToDoList();
  }

  addToDo(event: Event) {
    event.preventDefault(); // Prevent form submission
    if (this.newToDo) {
      this.toDoService.addToDo(this.newToDo);
      this.newToDo = '';
      this.toDoList = this.toDoService.getToDoList();
    }
  }

  deleteToDo(todo: string) {
    this.toDoService.deleteToDo(todo);
    this.toDoList = this.toDoService.getToDoList();
  }

  editToDoItem(index: number) {
    this.editIndex.push(index);
    this.editTodo = this.toDoList[index];
  }

  saveToDoItem(index: number) {
    if (this.editTodo) {
      this.toDoList[index] = this.editTodo;
      this.editTodo = '';
      this.editIndex = this.editIndex.filter(i => i !== index);
    }
  }
}