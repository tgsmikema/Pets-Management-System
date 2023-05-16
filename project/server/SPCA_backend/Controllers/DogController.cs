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




        [HttpPost("register")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult dogRegister(DogInDTO dogInDto)
        {
            bool isRegisterSuccessful = _repository.AddNewDog(dogInDto);
            return Ok("Dog successfully registered.");
        }




        [HttpDelete("delete")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminAndVetOnly")]
        public ActionResult deleteDog(int dogId)
        {
            bool isDeleteSuccessful = _repository.DeleteDog(dogId);
            return (isDeleteSuccessful ? Ok("Dog successfully deleted.") : NotFound("Dog of the requested ID does not exist"));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpGet("adminListAllCentres")]
        public ActionResult<IEnumerable<DogOutDTO>> getAllDogsAllCentres()
        {
            return Ok(_repository.ListAllDogsAllCentres());
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpGet("adminListOneCentre")]
        public ActionResult<IEnumerable<DogOutDTO>> listAllDogsInACentre(int centreId)
        {
            return Ok(_repository.ListAllDogsInACentre(centreId));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("userListOwnCentre")]
        public ActionResult<IEnumerable<DogOutDTO>> listAllDogsInOwnCentre()
        {
            int userCentreId = _repository.GetUserInfo(retrieveUserNameOfLoggedInUser()).CentreId;
            return Ok(_repository.ListAllDogsInACentre(userCentreId));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpGet("detailFromAllCentre")]
        public ActionResult<DogOutDTO> GetDogInformationAllCentres(int dogId)
        {
            DogOutDTO dogDTO = _repository.GetDogInformationAllCentres(dogId);
            if (dogDTO.Id == -1) { return NotFound("No Dog found with that Id"); }
            return Ok(dogDTO);
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("detailFromOwnCentre")]
        public ActionResult<DogOutDTO> GetDogInformationOwnCentre(int dogId)
        { 
            int userCentreId = _repository.GetUserInfo(retrieveUserNameOfLoggedInUser()).CentreId;
            DogOutDTO dogDTO = _repository.GetDogInformationOwnCentre(dogId, userCentreId);
            if (dogDTO.Id == -1) { return NotFound("No Dog found with that Id"); }
            return Ok(dogDTO);
        }




        [HttpPost("edit")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult editADogInfo(DogEditInDto dogEditInDto)
        {
            bool isValid = _repository.EditDogInformation(dogEditInDto);
            return (isValid ? Ok("Dog Information successfully edited.") : NotFound("Dog of the requested ID does not exist"));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminAndVetOnly")]
        [HttpGet("toggleFlag")]
        public ActionResult toggleFlag(int dogId)
        {
            bool isValid = _repository.ToggleDogFlag(dogId);
            return (isValid ? Ok("Dog Flag successfully toggled.") : NotFound("Dog of the requested ID does not exist"));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminAndVetOnly")]
        [HttpGet("toggleAlert")]
        public ActionResult toggleAlert(int dogId)
        {
            bool isValid = _repository.ToggleDogAlert(dogId);
            return (isValid ? Ok("Dog Alert successfully toggled.") : NotFound("Dog of the requested ID does not exist"));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpPost("invokeScaleRequest")]
        public ActionResult invokeScaleRequest(RequestInDto requestInDto)
        {
            bool isValid = _repository.AddNewRequest(requestInDto);
            return (isValid ? Ok("Request has been added") : NotFound("An error occured, please try again!"));
        }




        [HttpPost("addWeightFromScale")]
        public ActionResult addWeightFromScale(ScaleWeightRequestInDto scaleWeightRequestInDto)
        {
            bool isValid = _repository.AddWeightFromScaleToRequest(scaleWeightRequestInDto);
            return (isValid ? Ok("Weight has been added to the request.") : NotFound("An error occured, please try again!"));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("getCurrentWeightFromScale")]
        public ActionResult<DogWeightRequestOutDto> getCurrentWeightFromScale(int dogId)
        {
            DogWeightRequestOutDto dogWeightRequestOutDto = _repository.GetCurrentDogRequestWeight(dogId);
            return (dogWeightRequestOutDto == null ? NotFound("Dog Not being weighted!") : Ok(dogWeightRequestOutDto));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("saveCurrentWeight")]
        public ActionResult saveCurrentWeight(int dogId)
        {
            bool isValid = _repository.SaveCurrentWeight(dogId);
            return (isValid ? Ok("Weight has been added to the database.") : NotFound("No weight has been recorded or dog is not currently being weighted!"));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("getWeightHistory")]
        public ActionResult<IEnumerable<Weight>> getWeightHistory(int dogId)
        {
            return Ok(_repository.GetWeightHistoryForADog(dogId));
        }

        //----------------------------------------------------Helper Methods-----------------------------------------------------------------------------






        private string getUserNameFromHeader(string header)
        {
            var credentialBytes = Convert.FromBase64String(header);
            var credentials = Encoding.UTF8.GetString(credentialBytes).Split(":");
            var username = credentials[0];
            return username;
        }

        private string retrieveUserNameOfLoggedInUser()
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
            return userName;
        }

       

    }
}