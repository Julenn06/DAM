import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DataService } from '../../services/data.service';
import { Provincia } from '../../models/provincia.model';
import { Persona } from '../../models/persona.model';

@Component({
  selector: 'app-person',
  templateUrl: './person.html',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule]
})
export class PersonComponent implements OnInit {
  provincias: Provincia[] = [];
  personas: Persona[] = [];
  personasFiltrados: Persona[] = [];
  provinciaSeleccionada: number | null = null;
  filtroHabitantes: number | null = null;
  PersonaEditando: Persona | null = null;
  nuevoPersona: Partial<Persona> = {};

  constructor(private dataService: DataService) {}

  ngOnInit() {
    this.loadpersonas();
    this.loadFilters();
  }

  loadpersonas() {
    this.dataService.getPersonas().subscribe(data => {
      this.personas = data;
      this.filtrarPersonas();
    });
  }

  filtrarPersonas() {
    let temppersonas = this.personas;

    if (this.provinciaSeleccionada) {
      temppersonas = temppersonas.filter(p => p.id === this.provinciaSeleccionada);
    }

    if (this.filtroHabitantes) {
      temppersonas = temppersonas.filter(p => p.edad > this.filtroHabitantes!);
    }

    this.personasFiltrados = temppersonas;
    this.saveFilters();
  }

  saveFilters() {
    sessionStorage.setItem('filtroProvincia', this.provinciaSeleccionada?.toString() || '');
    sessionStorage.setItem('filtroHabitantes', this.filtroHabitantes?.toString() || '');
  }

  loadFilters() {
    const filtroProvincia = sessionStorage.getItem('filtroProvincia');
    if (filtroProvincia) {
      this.provinciaSeleccionada = parseInt(filtroProvincia, 10);
    }
    const filtroHabitantes = sessionStorage.getItem('filtroHabitantes');
    if (filtroHabitantes) {
      this.filtroHabitantes = parseInt(filtroHabitantes, 10);
    }
  }

  limpiarFiltros() {
    this.provinciaSeleccionada = null;
    this.filtroHabitantes = null;
    this.filtrarPersonas();
  }

  crearPersona() {
    this.dataService.createPersona(this.nuevoPersona as Persona).subscribe(() => {
      this.loadpersonas();
      this.nuevoPersona = {};
    });
  }

  editarPersona(Persona: Persona) {
    this.PersonaEditando = { ...Persona };
  }

  guardarPersona() {
    if (this.PersonaEditando) {
      this.dataService.updatePersona(this.PersonaEditando).subscribe(() => {
        this.loadpersonas();
        this.PersonaEditando = null;
      });
    }
  }

  cancelarEdicion() {
    this.PersonaEditando = null;
  }

  eliminarPersona(id: number) {
    this.dataService.deletePersona(id).subscribe(() => {
      this.loadpersonas();
    });
  }
}
