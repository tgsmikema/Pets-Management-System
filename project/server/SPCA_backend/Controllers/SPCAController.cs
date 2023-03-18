using Microsoft.AspNetCore.Mvc;
using SPCA_backend.Data;
using SPCA_backend.Model;
using Microsoft.AspNetCore.Authorization;
using System.Net.Mime;
using System.Security.Claims;

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

        [HttpGet("GetLoginPasswordForTestingPurpose")]
        public ActionResult demoFunction()
        {
            return Ok("admin 123; vets 123; volunteers 123");
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpGet("adminFunction")]
        public ActionResult adminFunction()
        {
            return Ok("logged In As ADMIN!");
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "VetsOnly")]
        [HttpGet("vetsFunction")]
        public ActionResult vetsFunction()
        {
            return Ok("logged In As VETS!");
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "VolunteersOnly")]
        [HttpGet("volunteersFunction")]
        public ActionResult volunteersFunction()
        {
            return Ok("logged In As VOLUNTEERS!");
        }

    }
}
