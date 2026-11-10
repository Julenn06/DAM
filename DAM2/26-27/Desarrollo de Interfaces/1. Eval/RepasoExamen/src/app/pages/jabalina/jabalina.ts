import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
type Athlete = { name: string; best: number };
@Component({
  selector: 'app-jabalina',
  imports: [RouterLink],
  templateUrl: './jabalina.html',
  styleUrl: './jabalina.css',
})
export class JabalinaComponent {
  protected readonly names = ['Ane', 'Iker', 'Lucía', 'Jon', 'Marta'];
  protected athletes = signal<Athlete[]>(this.create());
  protected round = signal(0);
  protected ranking() {
    return [...this.athletes()].sort((a, b) => b.best - a.best);
  }
  protected nextRound() {
    this.athletes.update((values) =>
      values.map((athlete) => ({
        ...athlete,
        best: Math.max(athlete.best, Number((40 + Math.random() * 50).toFixed(2))),
      })),
    );
    this.round.update((value) => value + 1);
  }
  protected reset() {
    this.athletes.set(this.create());
    this.round.set(0);
  }
  private create() {
    return this.names.map((name) => ({ name, best: 0 }));
  }
}
