using Microsoft.AspNetCore.Mvc;
using SPCA_backend.Data;
using SPCA_backend.Model;

namespace SPCA_backend.Controllers
{
    [Route("webapi")]
    [ApiController]
    public class SPCAController : Controller
    {
       private readonly ISPCARepo _repository;

       public SPCAController(ISPCARepo repository)
        {
            _repository = repository;
        }

        [HttpGet("demo")]
        public ActionResult demoFunction()
        {
            return Ok("Hello World");
        }
    }
}
