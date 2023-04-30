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
            return (checkNotExisting ? Ok("Scale successfully created.") : BadRequest("Scale already exists"));
        }




        [HttpGet("listAllScales")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult listAllScales()
        {
            IEnumerable<Scale> allScales = _repository.ListAllScales();
            return Ok(allScales);
        }




        [HttpDelete("deleteScale")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult deleteScale(int id)
        {
            bool checkExisting = _repository.DeleteScale(id);
            return (checkExisting ? Ok("Scale successfully deleted.") : NotFound("Scale with id " + id + " not found"));
        }




        [HttpPost("addNewCentre")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult addNewCentre(string name)
        {
            bool checkNotExisting = _repository.AddNewCentre(name);
            return (checkNotExisting ? Ok("Centre created") : BadRequest("Centre already exists"));
        }




        [HttpGet("listAllCentres")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult listAllCentres()
        {
            IEnumerable<Centre> listOfCentres = _repository.ListAllCentres();
            return Ok(listOfCentres);
        }




        [HttpDelete("deleteCentre")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult deleteCentre(int id)
        {
            bool checkExisting = _repository.DeleteCentre(id);
            return (checkExisting ? Ok("Centre deleted") : NotFound("Centre with id " + id + " does not exist"));
        }




        [HttpGet("thisWeekStats")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult<StatsOutDTO> getThisWeekStats(int centerId)
        {
            StatsOutDTO statsOutDTO = _repository.getCurrentWeekStats(centerId);
            return Ok(statsOutDTO);
        }




        [HttpPost("weeklyStats")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult<IEnumerable<StatsOutDTO>> getWeeklyStats(StatsInDTO statsInDTO)
        {
            IEnumerable<StatsOutDTO> statsOutDTOList = _repository.getWeeklyStats(statsInDTO);
            return Ok(statsOutDTOList);
        }



        [HttpPost("monthlyStatus")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult<IEnumerable<StatsOutDTO>> getMonthlyStatus(StatsInDTO statsInDTO)
        {
            IEnumerable<StatsOutDTO> statsOutDTOList = _repository.getMonthlyStats(statsInDTO);
            return Ok(statsOutDTOList);
        }

    }
}
