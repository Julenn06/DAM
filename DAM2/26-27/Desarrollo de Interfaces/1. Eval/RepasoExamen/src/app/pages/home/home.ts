import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomeComponent {
  protected readonly exercises = [
    {
      number: '01',
      path: '/secreto',
      level: 'Nivel ★★',
      title: 'Adivina el número secreto',
      summary: 'Siete intentos, pistas y récord de la partida.',
    },
    {
      number: '02',
      path: '/saltos',
      level: 'Nivel ★★',
      title: 'Saltos del tampón',
      summary: 'Simula saltos y observa sus estadísticas.',
    },
    {
      number: '03',
      path: '/tragaperras',
      level: 'Nivel ★★★',
      title: 'Máquina tragaperras',
      summary: 'Apuesta créditos y gana según tus símbolos.',
    },
    {
      number: '04',
      path: '/jabalina',
      level: 'Nivel ★★★',
      title: 'Final de jabalina',
      summary: 'Registra tres lanzamientos y ordena la clasificación.',
    },
    {
      number: '05',
      path: '/primitiva',
      level: 'Nivel ★★★★',
      title: 'Primitiva',
      summary: 'Juega combinaciones y consulta el historial.',
    },
  ];
}
