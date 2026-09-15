import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LibrosRemoteService } from '../../service/libros.remote';
import { Libro } from '../../libro/Libro';

@Component({
  selector: 'app-biblioteca-view',
  imports: [FormsModule],
  templateUrl: './biblioteca-view.html',
  styleUrl: './biblioteca-view.css',
})
export class BibliotecaView {

  titulo: string = '';
  autor: string = '';
  genero: string = '';
  ano: number = 0;
  tituloNuevo: string = '';
  autorNuevo: string = '';
  generoNuevo: string = '';
  anoNuevo: number = 0;
  idLibro: number = 0;
  tituloBuscar: string = '';
  autorBuscar: string = '';
  generoSeleccionado: string = '';
  numeroAno: number = 0;
  todosLosLibros: Libro[] = [];

  constructor(private cdr: ChangeDetectorRef, private remote: LibrosRemoteService) {}

  anadirLibro() {
    if (!this.titulo.trim()) {
      console.error('El título es obligatorio');
      return;
    }
    this.remote.createLibro(this.titulo.trim(), this.autor.trim(), this.genero.trim(), this.ano).subscribe({
      next: (libroCreado) => {
        console.log('Libro creado exitosamente:', libroCreado);
        this.verLibros();
        this.limpiarDatos();
      },
      error: (err) => console.error('Error creando libro', err)
    });
  }

  limpiarDatos() {
    this.titulo = '';
    this.autor = '';
    this.genero = '';
    this.ano = 0;
  }

  nuevosDatos() {
    if (!this.tituloNuevo.trim()) {
      console.error('El título es obligatorio');
      return;
    }
    const changes: Partial<Libro> = {};
    if (this.tituloNuevo.trim()) changes.titulo = this.tituloNuevo.trim();
    if (this.autorNuevo.trim()) changes.autor = this.autorNuevo.trim();
    if (this.generoNuevo.trim()) changes.genero = this.generoNuevo.trim();
    if (this.anoNuevo) changes.ano = this.anoNuevo;

    this.remote.updateLibro(this.idLibro, changes).subscribe({
      next: (libroActualizado) => {
        console.log('Libro actualizado exitosamente:', libroActualizado);
        this.verLibros();
        this.limpiarDatosEdicion();
      },
      error: (err) => console.error('Error actualizando libro', err)
    });
  }

  limpiarDatosEdicion() {
    this.tituloNuevo = '';
    this.autorNuevo = '';
    this.generoNuevo = '';
    this.anoNuevo = 0;
  }

  verLibros() {
    this.remote.getAllLibros().subscribe({
      next: (libros) => {
        this.todosLosLibros = libros;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error cargando Libros', err);
      }
    });
  }

  verLibroPorId() {
    this.remote.getLibroById(this.idLibro).subscribe({
      next: (libro) => {
        this.todosLosLibros = [libro];
        this.cdr.markForCheck();
      },
      error: () => {
        this.todosLosLibros = [];
      }
    });
  }

  verLibrosPorAutor() {
    if (!this.autorBuscar || this.autorBuscar.trim() === '') {
      console.error('Autor inválido');
      return;
    }
    this.remote.getLibrosByAutor(this.autorBuscar.trim()).subscribe({
      next: (libros) => {
        this.todosLosLibros = libros;
        this.cdr.markForCheck();
        this.autorBuscar = '';
      },
      error: (err) => {
        console.error('Error cargando Libros por autor', err);
        this.todosLosLibros = [];
        this.cdr.markForCheck();
      }
    });
  }

  verLibrosPorGenero() {
    if (!this.generoSeleccionado) {
      console.error('Selecciona un género');
      return;
    }
    this.remote.getLibrosByGenero(this.generoSeleccionado).subscribe({
      next: (libros: Libro[]) => {
        this.todosLosLibros = libros;
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('Error cargando Libros por género', err);
        this.todosLosLibros = [];
        this.cdr.markForCheck();
      }
    });
  }

  verLibrosMayoresDeUnAno() {
    if (this.numeroAno <= 0) {
      console.log("El año debe ser mayor a 0");
      return;
    }
    this.remote.getLibrosByAnoGreaterThan(this.numeroAno).subscribe({
      next: (libros) => {
        this.todosLosLibros = libros;
        this.cdr.markForCheck();
        this.numeroAno = 0;
      },
      error: (err) => {
        console.error('Error cargando Libros por año', err);
        this.todosLosLibros = [];
        this.cdr.markForCheck();
      }
    });
  }

  verLibrosAnoPar() {
    this.remote.getLibrosByAnoPar().subscribe({
      next: (libros) => {
        this.todosLosLibros = libros;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error cargando Libros con año par', err);
        this.todosLosLibros = [];
        this.cdr.markForCheck();
      }
    });
  }

  eliminarLibro() {
    if (!this.idLibro) {
      console.error('ID es obligatorio');
      return;
    }
    this.remote.deleteLibro(this.idLibro).subscribe({
      next: () => {
        console.log('Libro eliminado exitosamente');
        this.verLibros();
        this.idLibro = 0;
      },
      error: (err) => console.error('Error eliminando libro', err)
    });
  }

  eliminarLibroPorTitulo() {
    if (!this.tituloBuscar || this.tituloBuscar.trim() === '') {
      console.error('Título es obligatorio');
      return;
    }
    this.remote.deleteLibroByTitulo(this.tituloBuscar.trim()).subscribe({
      next: () => {
        console.log('Libro eliminado exitosamente');
        this.verLibros();
        this.tituloBuscar = '';
      },
      error: (err) => console.error('Error eliminando libro', err)
    });
  }
}
