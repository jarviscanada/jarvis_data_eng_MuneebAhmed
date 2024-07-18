import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToDoService {
  private localStorageKey = 'toDoList';

  constructor() {
    this.loadFromLocalStorage();
  }

  private toDoList: string[] = [];

  getToDoList(): string[] {
    return this.toDoList;
  }

  addToDo(todo: string): void {
    this.toDoList.push(todo);
    this.saveToLocalStorage();
  }

  deleteToDo(todo: string): void {
    this.toDoList = this.toDoList.filter(item => item !== todo);
    this.saveToLocalStorage();
  }

  getToDoById(id: number): string | undefined {
    return this.toDoList[id];
  }

  private saveToLocalStorage(): void {
    localStorage.setItem(this.localStorageKey, JSON.stringify(this.toDoList));
  }

  private loadFromLocalStorage(): void {
    const storedToDoList = localStorage.getItem(this.localStorageKey);
    if (storedToDoList) {
      this.toDoList = JSON.parse(storedToDoList);
    }
  }
}