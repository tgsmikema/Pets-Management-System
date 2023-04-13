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
        /*[Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]*/
        public ActionResult dogRegister(DogInDTO dogInDto)
        {
            bool isRegisterSuccessful = _repository.AddNewDog(dogInDto);
            return Ok("Dog successfully registered.");
        }

        [HttpPost("register2")]
        public ActionResult userRegister2(UserInDto userLoginInDto)
        {
            bool isRegisterSuccessful = _repository.AddNewUser(userLoginInDto);

            if (isRegisterSuccessful)
            {
                return Ok("User successfully registered.");
            }
            else
            {
                return Ok("Username not available. Please Try again.");
            }
        }
    }
}