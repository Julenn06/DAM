using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace pruebaswagger
{
    [ApiController]
    [Route("api/[controller]")]
    public class MetodosService : ControllerBase
    {
        private readonly Metodos _metodos;

        public MetodosService(Metodos metodos)
        {
            _metodos = metodos;
        }


        [HttpGet("hello")]
        [AllowAnonymous]
        public IActionResult GetHelloWorld()
        {
            var mensaje = _metodos.GetMensaje();
            return Ok(mensaje);
        }

        [HttpPost("echo")]
        [AllowAnonymous]
        public IActionResult PostEcho([FromBody] string nombre)
        {
            var mensaje = _metodos.PostMensaje(nombre);
            return Ok(mensaje);
        }

        [HttpGet("sumar")]
        [AllowAnonymous]
        public IActionResult Sumar(int a, int b)
        {
            var resultado = _metodos.Sumar(a, b);
            return Ok(new { operacion = "suma", a, b, resultado });
        }

        [HttpGet("restar")]
        [AllowAnonymous]
        public IActionResult Restar(int a, int b)
        {
            var resultado = _metodos.Restar(a, b);
            return Ok(new { operacion = "resta", a, b, resultado });
        }

        [HttpGet("multiplicar")]
        [AllowAnonymous]
        public IActionResult Multiplicar(int a, int b)
        {
            var resultado = _metodos.Multiplicar(a, b);
            return Ok(new { operacion = "multiplicacion", a, b, resultado });
        }

        [HttpGet("dividir")]
        [AllowAnonymous]
        public IActionResult Dividir(int a, int b)
        {
            try
            {
                var resultado = _metodos.Dividir(a, b);
                return Ok(new { operacion = "division", a, b, resultado });
            }
            catch (DivideByZeroException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        [HttpGet("potencia")]
        [AllowAnonymous]
        public IActionResult Potencia(int a, int b)
        {
            var resultado = _metodos.Potencia(a, b);
            return Ok(new { operacion = "potencia", a, b, resultado });
        }

        [HttpGet("factorial")]
        [AllowAnonymous]
        public IActionResult Factorial(int n)
        {
            try
            {
                var resultado = _metodos.Factorial(n);
                return Ok(new { operacion = "factorial", n, resultado });
            }
            catch (ArgumentException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        [HttpGet("esprimo")]
        [AllowAnonymous]
        public IActionResult EsPrimo(int n)
        {
            var resultado = _metodos.EsPrimo(n);
            return Ok(new { operacion = "esprimo", n, resultado });
        }

        [HttpGet("fibonacci")]
        [AllowAnonymous]
        public IActionResult Fibonacci(int n)
        {
            try
            {
                var resultado = _metodos.Fibonacci(n);
                return Ok(new { operacion = "fibonacci", n, resultado });
            }
            catch (ArgumentException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        [HttpGet("invertircadena")]
        [AllowAnonymous]
        public IActionResult InvertirCadena(string s)
        {
            try
            {
                var resultado = _metodos.InvertirCadena(s);
                return Ok(new { operacion = "invertircadena", s, resultado });
            }
            catch (ArgumentNullException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        [HttpGet("palindromo")]
        [AllowAnonymous]
        public IActionResult EsPalindromo(string s)
        {
            try
            {
                var resultado = _metodos.EsPalindromo(s);
                return Ok(new { operacion = "palindromo", s, resultado });
            }
            catch (ArgumentNullException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        [HttpGet("contarVocales")]
        [AllowAnonymous]
        public IActionResult ContarVocales(string s)
        {
            try
            {
                var resultado = _metodos.ContarVocales(s);
                return Ok(new { operacion = "contarVocales", s, resultado });
            }
            catch (ArgumentNullException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }
    }
}
