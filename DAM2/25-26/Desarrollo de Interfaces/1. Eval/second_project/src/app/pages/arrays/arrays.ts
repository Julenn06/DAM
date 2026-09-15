import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

enum GenderEnum {
  M = 'M',
  F = 'F'
}

interface IWorker {
  id: number;
  name: string;
  age: number;
  gender: GenderEnum;
  salary: number;
}

@Component({
  selector: 'app-arrays',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './arrays.html',
  styleUrls: ['./arrays.css']
})
export class Arrays {
  resultadoActivo: string = '';
  workers: IWorker[] = [
    {
      id: 1,
      name: '1. Langilea',
      age: 20,
      gender: GenderEnum.M,
      salary: 20000
    },
    {
      id: 2,
      name: '2. Langilea',
      age: 20,
      gender: GenderEnum.M,
      salary: 20000
    },
    {
      id: 3,
      name: '3. Langilea',
      age: 20,
      gender: GenderEnum.M,
      salary: 19000
    },
    {
      id: 4,
      name: '4. Langilea',
      age: 20,
      gender: GenderEnum.F,
      salary: 21000
    }
  ];

  // Ariketa 1 (buscar ID 1)
  workerAriketa1: IWorker | undefined;

  workerNames: IWorker[] | undefined;

  buscarID() {
  this.workerAriketa1 = this.buscarIDUser(1);
  this.resultadoActivo = 'id';
  }

  buscarIDUser(idWorker: number): IWorker | undefined {
    return this.workers.find(worker => worker.id === idWorker);
  }

  // Ariketa 2 (buscar nombre de F)

  langileF: IWorker[] = [];

  buscarGeneroF() {
  this.langileF = this.workers.filter(worker => worker.gender === GenderEnum.F);
  this.resultadoActivo = 'femenino';
  }

  // Ariketa 3 (calcular salario total)

  langileM: IWorker[] = [];

  buscarGeneroM() {
  this.langileM = this.workers.filter(worker => worker.gender === GenderEnum.M);
  this.resultadoActivo = 'masculino';
  }


  //Ariketa 4 (Todos los nombres de los trabajadores)
  langileGuztiak: IWorker[] = [];

  mostrarNombres() {
  this.langileGuztiak = [...this.workers];
  this.resultadoActivo = 'nombres';
  }


  //Ariketa 5 (Salario mayor a 20000)
  buscarSalarioMayor() {
  this.langileM = this.workers.filter(worker => worker.salary > 20000);
  alert(this.langileM.map(worker => worker.name).join(', ') + ', Salario: ' + this.langileM.map(worker => worker.salary).join(', '));
  }

  //Ariketa 6 (Media de todos los salarios)
  calcularMediaSalarios() {
  const totalSalarios = this.workers.reduce((total, worker) => total + worker.salary, 0);
  alert(`Suma total de los salarios: ${totalSalarios}`);
  }

  //Ariketa 7 (Mostrar la ultima persona de un genero)
  mostrarUltimoPorGenero() {
  const ultimoMasculino = [...this.workers].reverse().find(worker => worker.gender === GenderEnum.M);
  this.workerAriketa1 = ultimoMasculino;
  this.resultadoActivo = 'ultimoMasculino';
  }

  //Ariketa 8 (Mostrar el primero que cobre 20000 con su nombre)
  langile20000: IWorker[] = [];
  mostrarTrabajadorPorSalario() {
  const trabajador20000 = this.workers.find(worker => worker.salary === 20000);
  this.langile20000 = trabajador20000 ? [trabajador20000] : [];
  this.resultadoActivo = 'primero20000';
  }

    //Ariketa 9 (Mostrar el ultimo que cobre 20000 con su nombre)

    langileAzkena20000: IWorker | undefined;
    mostrarUltimoTrabajadorPorSalario() {
  const ultimoTrabajador20000 = [...this.workers].reverse().find(worker => worker.salary === 20000);
  this.langileAzkena20000 = ultimoTrabajador20000 ? ultimoTrabajador20000 : undefined;
  this.resultadoActivo = 'ultimo20000';
    }

    //Ariketa 10 (Buscar ID 5)
    buscarID5() {
      const workerID5 = this.buscarIDUser(5);
      if (workerID5) {
        this.workerAriketa1 = workerID5;
        this.resultadoActivo = 'id5';
      } else {
        alert('No se encontró ningún trabajador con ID 5.');
      }
    }

  //Ariketa 11 (Buscar todos los trabajadores)
  todosLosTrabajadores: IWorker[] = [];

  buscarTodosLosTrabajadores() {
  this.todosLosTrabajadores = [...this.workers];
  this.resultadoActivo = 'todos';
  }
  //Ariketa 12 (Operaciones de array sin modificar el original)
  probarPOP() {
    const trabajadoresCopia = [...this.todosLosTrabajadores.length ? this.todosLosTrabajadores : this.workers];
    const trabajadorEliminado = trabajadoresCopia.pop();
    if (trabajadorEliminado) {
      alert(`Trabajador eliminado: ${trabajadorEliminado.name}`);
      this.todosLosTrabajadores = trabajadoresCopia;
      this.resultadoActivo = 'todos';
    }
  }

  probarPUSH() {
    const trabajadoresCopia = [...this.todosLosTrabajadores.length ? this.todosLosTrabajadores : this.workers];
    const nuevoTrabajador: IWorker = {
      id: trabajadoresCopia.length + 1,
      name: `${trabajadoresCopia.length + 1}. Langilea`,
      age: 25,
      gender: GenderEnum.M,
      salary: 22000
    };
    trabajadoresCopia.push(nuevoTrabajador);
    alert(`Nuevo trabajador añadido: ${nuevoTrabajador.name}`);
    this.todosLosTrabajadores = trabajadoresCopia;
    this.resultadoActivo = 'todos';
  }

  probarSHIFT() {
    const trabajadoresCopia = [...this.todosLosTrabajadores.length ? this.todosLosTrabajadores : this.workers];
    const trabajadorEliminado = trabajadoresCopia.shift();
    if (trabajadorEliminado) {
      alert(`Trabajador eliminado: ${trabajadorEliminado.name}`);
      this.todosLosTrabajadores = trabajadoresCopia;
      this.resultadoActivo = 'todos';
    }
  }

  probarUNSHIFT() {
    const trabajadoresCopia = [...this.todosLosTrabajadores.length ? this.todosLosTrabajadores : this.workers];
    const nuevoTrabajador: IWorker = {
      id: trabajadoresCopia.length + 1,
      name: `${trabajadoresCopia.length + 1}. Langilea`,
      age: 30,
      gender: GenderEnum.M,
      salary: 23000
    };
    trabajadoresCopia.unshift(nuevoTrabajador);
    alert(`Nuevo trabajador añadido: ${nuevoTrabajador.name}`);
    this.todosLosTrabajadores = trabajadoresCopia;
    this.resultadoActivo = 'todos';
  }

  probarSLICE() {
  const trabajadoresCopia = [...this.todosLosTrabajadores.length ? this.todosLosTrabajadores : this.workers];
  const trabajadoresSlice = trabajadoresCopia.slice(1, 3);
  alert(`Trabajadores obtenidos con slice: ${trabajadoresSlice.map(t => t.name).join(', ')}`);
  this.todosLosTrabajadores = trabajadoresSlice;
  this.resultadoActivo = 'todos';
  }

  probarSPLICE() {
  const trabajadoresCopia = [...this.todosLosTrabajadores.length ? this.todosLosTrabajadores : this.workers];
  const trabajadoresSplice = trabajadoresCopia.splice(1, 2);
  alert(`Trabajadores eliminados con splice: ${trabajadoresSplice.map(t => t.name).join(', ')}`);
  this.todosLosTrabajadores = trabajadoresCopia;
  this.resultadoActivo = 'todos';
  }
}