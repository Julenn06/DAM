import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CochesRemoteService } from '../../service/coches.remote';
import { Coche } from '../../coche/Coche';

@Component({
  selector: 'app-first-view',
  imports: [FormsModule],
  templateUrl: './first-view.html',
  styleUrl: './first-view.css',
})
export class FirstView {
  nombre: string = '';
  tipo: string = '';
  ano: number = 0;
  nombreNuevo: string = '';
  tipoNuevo: string = '';
  anoNuevo: number = 0;
  idCoche: number = 0;
  nombreCoche: any;
  numeroAno: number = 0;
  todosLosCoches: Coche[] = [];
  tipoSeleccionado: string = '';

  constructor(
    private cdr: ChangeDetectorRef,
    private remote: CochesRemoteService,
    private router: Router,
  ) {}

  anadirCoche() {
    if (!this.nombre.trim()) {
      console.error('El nombre es obligatorio');
      return;
    }

    this.remote.createCoche(this.nombre.trim(), this.tipo.trim(), this.ano).subscribe({
      next: (cocheCreado) => {
        console.log('Coche creado exitosamente:', cocheCreado);
        this.verCoches();
        this.limpiarDatos();
      },
      error: (err) => console.error('Error creando coche', err),
    });
  }
  limpiarDatos() {
    this.nombre = '';
    this.tipo = '';
    this.ano = 0;
  }
  nuevosDatos() {
    if (!this.nombreNuevo.trim()) {
      console.error('El nombre es obligatorio');
      return;
    }

    const changes: Partial<Coche> = {};
    if (this.nombreNuevo.trim()) changes.name = this.nombreNuevo.trim();
    if (this.tipoNuevo.trim()) changes.tipo = this.tipoNuevo.trim();
    if (this.anoNuevo) changes.ano = this.anoNuevo;

    this.remote.updateCoche(this.idCoche, changes).subscribe({
      next: (cocheCreado) => {
        console.log('Coche creado exitosamente:', cocheCreado);
        this.verCoches();
        this.limpiarDatos();
      },
      error: (err) => console.error('Error creando coche', err),
    });
  }

  limpiarDatosEdicion() {
    this.nombreNuevo = '';
    this.tipoNuevo = '';
    this.anoNuevo = 0;
  }

  verCoches() {
    this.remote.getAllCoches().subscribe({
      next: (hs) => {
        this.todosLosCoches = hs;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error cargando Coches', err);
      },
    });
  }
  verCochePorId() {
    this.remote.getCocheById(this.idCoche).subscribe({
      next: (hs) => {
        this.todosLosCoches = [hs];
        this.cdr.markForCheck();
      },
      error: () => {
        this.todosLosCoches = [];
      },
    });
  }
  verCochePorNombre() {
    if (!this.nombreCoche || this.nombreCoche == null) {
      console.error('Nombre inválido');
      return;
    }

    this.remote.getCocheByName(this.nombreCoche).subscribe({
      next: (coche) => {
        if (coche && coche.id !== undefined && coche.id !== null) {
          this.todosLosCoches = [coche];
        } else {
          this.todosLosCoches = [];
        }
        this.cdr.markForCheck();
        this.nombreCoche = '';
      },
      error: (err) => {
        console.error('Error cargando Coche por Name', err);
        this.todosLosCoches = [];
        this.cdr.markForCheck();
      },
    });
  }

  verCochesMayoresDeUnAno() {
    if (this.numeroAno == 0) {
      console.log('La fecha debe ser mayor a 0');
    }

    this.remote.getCochesByYearGreaterThan(this.numeroAno).subscribe({
      next: (coche) => {
        if (coche !== undefined) {
          this.todosLosCoches = coche;
        } else {
          this.todosLosCoches = [];
        }
        this.cdr.markForCheck();
        this.numeroAno = 0;
      },
      error: (err) => {
        console.error('Error cargando Coche por Name', err);
        this.todosLosCoches = [];
        this.cdr.markForCheck();
      },
    });
  }

  eliminarCoche() {
    if (!this.idCoche) {
      console.error('ID es obligatorio');
      return;
    }
    this.remote.deleteCoche(this.idCoche).subscribe({
      next: () => {
        console.log('Coche eliminado exitosamente');
        this.verCoches();
        this.idCoche = 0;
      },
      error: (err) => console.error('Error eliminando coche', err),
    });
  }
  eliminarCochePorNombre() {
    if (!this.nombreCoche || this.nombreCoche.trim() === '') {
      console.error('Nombre es obligatorio');
      return;
    }
    this.remote.deleteCocheByName(this.nombreCoche.trim()).subscribe({
      next: () => {
        console.log('Coche eliminado exitosamente');
        this.verCoches();
        this.nombreCoche = '';
      },
      error: (err) => console.error('Error eliminando coche', err),
    });
  }
  unPar() {
    this.remote.getCochesWithEvenYear().subscribe({
      next: (coches) => {
        this.todosLosCoches = coches;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error cargando Coches con año par', err);
        this.todosLosCoches = [];
        this.cdr.markForCheck();
      },
    });
  }

  verCochesPorTipo() {
    if (!this.tipoSeleccionado) {
      console.error('Selecciona un tipo');
      return;
    }
    this.remote.getCochesByTipo(this.tipoSeleccionado).subscribe({
      next: (coches: Coche[]) => {
        this.todosLosCoches = coches;
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('Error cargando Coches por tipo', err);
        this.todosLosCoches = [];
        this.cdr.markForCheck();
      },
    });
  }
}
