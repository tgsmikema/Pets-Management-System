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
        public ActionResult scaleRegister(ScaleInDTO scale)
        {
            _repository.AddNewScale(scale);
            return Ok("User successfully registered.");

        }


        //-----------------------------Helper Methods---------------------------------

        private string getUserNameFromHeader(string header)
        {
            var credentialBytes = Convert.FromBase64String(header);
            var credentials = Encoding.UTF8.GetString(credentialBytes).Split(":");
            var username = credentials[0];

            return username;
        }

    }
}
