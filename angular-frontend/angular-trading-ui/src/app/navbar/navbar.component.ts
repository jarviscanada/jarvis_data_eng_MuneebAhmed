import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, Event } from '@angular/router';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faAddressBook, faBook } from '@fortawesome/free-solid-svg-icons';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isDashboardRoute: boolean = false;
  isQuotesRoute: boolean = false;

  constructor(private router: Router, library: FaIconLibrary) {
    library.addIcons(faAddressBook, faBook);
  }

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.isDashboardRoute = event.urlAfterRedirects === '/dashboard';
        this.isQuotesRoute = event.urlAfterRedirects === '/quotes';
      }
    });
  }
}