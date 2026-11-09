import { computed, Service } from '@angular/core';
import { Gimnasioa } from '../modelo/Gimnasioa';
import { httpResource } from '@angular/common/http';

@Service()
export class Datuak {
  private readonly datuak = httpResource<Gimnasioa>(() => 'gimnasioa_komuna.json');

  readonly gimnasioa = computed(() => this.datuak.value());
  readonly kargatzen = computed(() => this.datuak.isLoading());
  readonly errorea = computed(() => this.datuak.error());
}
