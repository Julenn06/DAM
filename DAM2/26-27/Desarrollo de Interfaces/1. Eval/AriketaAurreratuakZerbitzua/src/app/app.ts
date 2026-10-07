import { Component, signal } from '@angular/core';
import { Zerrenda } from './pages/zerrenda/zerrenda';
import { Gehitu } from './pages/gehitu/gehitu';

@Component({
  imports: [Zerrenda, Gehitu],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('AriketaAurreratuakZerbitzua');
}
