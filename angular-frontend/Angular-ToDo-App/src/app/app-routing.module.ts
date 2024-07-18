import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ToDoPageComponent } from './to-do-page/to-do-page.component';
import { ToDoDetailComponent } from './to-do-detail/to-do-detail.component';

const routes: Routes = [
  { path: '', component: ToDoPageComponent },
  { path: 'todo/:id', component: ToDoDetailComponent },
  { path: 'todo', component: ToDoPageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
