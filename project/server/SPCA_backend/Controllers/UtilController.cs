using Microsoft.AspNetCore.Mvc;
using SPCA_backend.Data;
using SPCA_backend.Dtos;
using SPCA_backend.Model;
using Microsoft.AspNetCore.Authorization;
using System.Net.Mime;
using System.Security.Claims;
using Microsoft.AspNetCore.Cors;
using System.Text;
using System;

namespace SPCA_backend.Controllers
{
    // controller class, that can be thought of sub-route of the node+express.
    // enable CORS for this controller
    [EnableCors("_myAllowSpecificOrigins")]
    [Route("util")]
    [ApiController]
    public class UtilController : Controller
    {
        private readonly ISPCARepo _repository;

        public UtilController(ISPCARepo repository)
        {
            _repository = repository;
        }


        [HttpPost("addNewScale")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult addNewScale(ScaleInDTO scale)
        {
            _repository.AddNewScale(scale);
            return Ok("User successfully registered.");

        }

        [HttpPost("addNewCentre")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult addNewCentre(string name)
        {
            bool checkNotExisting = _repository.AddNewCentre(name);
            if (checkNotExisting)
            {
                return Ok("Centre created");
            }
            else
            {
                return BadRequest("Centre already exists");
            }

        }

    }
}
