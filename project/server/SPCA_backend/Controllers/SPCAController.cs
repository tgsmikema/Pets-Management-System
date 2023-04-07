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
    [Route("api")]
    [ApiController]
    public class SPCAController : Controller
    {
       private readonly ISPCARepo _repository;

       public SPCAController(ISPCARepo repository)
        {
            _repository = repository;
        }

        [HttpGet("testing")]
        public ActionResult demoFunction()
        {
            return Ok("This is a 770 Team 2 Hosted backend API, you are connected if you can see this message! :-) Other API endpoints are being built right now....");
        }

        [HttpPost("register")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult userRegister(UserLoginInDto userLoginInDto)
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

        [HttpPost("register2")]
        public ActionResult userRegister2(UserLoginInDto userLoginInDto)
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


        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("login")]
        public ActionResult<UserLoginOutDto> userLogin()
        {
            ClaimsIdentity ci = HttpContext.User.Identities.FirstOrDefault();
            string userName = "";
            string userType = "";
            string token = "";
            if (ci.FindFirst("admin") != null)
            {
                token = ci.FindFirst("admin").Value;
                userName = getUserNameFromHeader(token);
                userType = "admin";
            }
            else if (ci.FindFirst("vet") != null)
            {
                token = ci.FindFirst("vet").Value;
                userName = getUserNameFromHeader(token);
                userType = "vet";
            }
            else if (ci.FindFirst("volunteer") != null)
            {
                token = ci.FindFirst("volunteer").Value;
                userName = getUserNameFromHeader(token);
                userType = "volunteer";
            }
                
                return Ok(new UserLoginOutDto
                {
                    UserName = userName,
                    UserType = userType,
                    Token = token
                });

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
