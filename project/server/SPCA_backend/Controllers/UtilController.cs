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
            bool checkNotExisting = _repository.AddNewScale(scale);
            if (checkNotExisting)
            {
                return Ok("Scale successfully created.");
            }
            else
            {
                return BadRequest("Scale already exists");
            }

        }

        [HttpPost("listAllScales")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult listAllScales()
        {
            IEnumerable<Scale> allScales = _repository.ListAllScales();
            return Ok(allScales);
        }

        [HttpPost("deleteScale")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult deleteScale(int id)
        {
            bool checkExisting = _repository.DeleteScale(id);
            if (checkExisting)
            {
                return Ok("Scale successfully deleted.");
            }
            else
            {
                return NotFound("Scale with id " + id + " not found");
            }

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

        [HttpPost("listAllCentres")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult listAllCentres()
        {
            IEnumerable<Centre> listOfCentres = _repository.ListAllCentres();
            return Ok(listOfCentres);
        }

        [HttpPost("deleteCentre")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult deleteCentre(int id)
        {
            bool checkExisting = _repository.DeleteCentre(id);
            if (checkExisting)
            {
                return Ok("Centre deleted");
            }
            else
            {
                return NotFound("Centre with id " + id + " does not exist");
            }

        }

    }
}
