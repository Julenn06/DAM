import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-primitiva',
  imports: [RouterLink],
  templateUrl: './primitiva.html',
  styleUrl: './primitiva.css',
})
export class PrimitivaComponent {
  protected numbers = Array.from({ length: 49 }, (_, index) => index + 1);
  protected selected = signal<number[]>([]);
  protected message = signal('');
  protected played = signal(0);
  protected average = signal(0);
  protected toggle(number: number) {
    this.selected.update((values) =>
      values.includes(number)
        ? values.filter((value) => value !== number)
        : values.length < 6
          ? [...values, number]
          : values,
    );
  }
  protected randomPick() {
    this.selected.set(this.shuffle([...this.numbers]).slice(0, 6));
    this.message.set('Combinación preparada');
  }
  protected draw() {
    const result = this.shuffle([...this.numbers]).slice(0, 6);
    const hits = result.filter((number) => this.selected().includes(number)).length;
    this.played.update((value) => value + 1);
    this.average.update(
      (value) => Math.round(((value * (this.played() - 1) + hits) / this.played()) * 10) / 10,
    );
    this.message.set(`Has acertado ${hits} de 6 números.`);
  }
  protected simulate() {
    let totalHits = 0;
    for (let index = 0; index < 1000; index++) {
      const result = this.shuffle([...this.numbers]).slice(0, 6);
      totalHits += result.filter((number) => this.selected().includes(number)).length;
    }
    const previousPlayed = this.played();
    const newPlayed = previousPlayed + 1000;
    this.played.set(newPlayed);
    this.average.set(
      Math.round(((this.average() * previousPlayed + totalHits) / newPlayed) * 10) / 10,
    );
    this.message.set('Simulación de 1000 sorteos completada.');
  }
  private shuffle(values: number[]) {
    return values.sort(() => Math.random() - 0.5);
  }
}
