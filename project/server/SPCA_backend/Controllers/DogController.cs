using Microsoft.AspNetCore.Mvc;
using SPCA_backend.Data;
using SPCA_backend.Dtos;
using SPCA_backend.Model;
using Microsoft.AspNetCore.Authorization;
using System.Net.Mime;
using System.Security.Claims;
using Microsoft.AspNetCore.Cors;
using System.Text;

namespace SPCA_backend.Controllers
{
    // controller class, that can be thought of sub-route of the node+express.
    // enable CORS for this controller
    [EnableCors("_myAllowSpecificOrigins")]
    [Route("dog")]
    [ApiController]
    public class DogController : Controller
    {
        private readonly ISPCARepo _repository;

        public DogController(ISPCARepo repository)
        {
            _repository = repository;
        }

        [HttpGet("testing")]
        public ActionResult demoFunction()
        {
            return Ok("This is a 770 Team 2 Hosted backend API, you are connected if you can see this message! :-) Other API endpoints are being built right now....");
        }

        [HttpGet("testing2")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult demoFunction2()
        {
            return Ok("Admin function!!!");
        }


        [HttpPost("register")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult dogRegister(DogInDTO dogInDto)
        {
            bool isRegisterSuccessful = _repository.AddNewDog(dogInDto);
            return Ok("Dog successfully registered.");
        }

        [HttpPost("delete")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult deleteDog(int dogId)
        {
            bool isDeleteSuccessful = _repository.DeleteDog(dogId);
            if (isDeleteSuccessful)
            {
                return Ok("Dog successfully deleted.");
            }
            else
            {
                return NotFound("Dog of the requested ID does not exist");
            }
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpGet("getAllDogsAllCentres")]
        public ActionResult<IEnumerable<DogOutDTO>> getAllDogsAllCentres()
        {
            return Ok(_repository.ListAllDogsAllCentres());
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpGet("getAllDogsInACentreAdmin")]
        public ActionResult<IEnumerable<DogOutDTO>> listAllDogsInACentre(int centreId)
        {
            return Ok(_repository.ListAllDogsInACentre(centreId));
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("getAllDogsInOwnCentre")]
        public ActionResult<IEnumerable<DogOutDTO>> listAllDogsInOwnCentre()
        {
            ClaimsIdentity ci = HttpContext.User.Identities.FirstOrDefault();
            string userName = "";
            if (ci.FindFirst("admin") != null)
            {
                userName = getUserNameFromHeader(ci.FindFirst("admin").Value);
            }
            else if (ci.FindFirst("vet") != null)
            {
                userName = getUserNameFromHeader(ci.FindFirst("vet").Value);
            }
            else if (ci.FindFirst("volunteer") != null)
            {
                userName = getUserNameFromHeader(ci.FindFirst("volunteer").Value);
            }
            int userCentreId = _repository.GetUserInfo(userName).CentreId;

            return Ok(_repository.ListAllDogsInACentre(userCentreId));
        }


        //-----------------------------Helper Methods---------------------------------

        private string getUserNameFromHeader(string header)
        {
            var credentialBytes = Convert.FromBase64String(header);
            var credentials = Encoding.UTF8.GetString(credentialBytes).Split(":");
            var username = credentials[0];

            return username;
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "Admin")]
        [HttpGet("getDog")]
        public ActionResult<DogOutDTO> GetDogInformationAllCentres(int dogId)
        {
            DogOutDTO dogDTO = _repository.GetDogInformationAllCentres(dogId);
            if (dogDTO.Id == -1){
                return NotFound("No Dog found with that Id");
            }
            return Ok(dogDTO);
        }

    }
}