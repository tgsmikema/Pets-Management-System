using Microsoft.AspNetCore.Mvc;
using SPCA_backend.Data;
using SPCA_backend.Dtos;
using SPCA_backend.Model;
using Microsoft.AspNetCore.Authorization;
using System.Net.Mime;
using System.Security.Claims;

namespace SPCA_backend.Controllers
{
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
            return Ok("admin 123; vet 123; volunteer 123");
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


        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpPost("login")]
        public ActionResult<UserLoginOutDto> userLogin()
        {
            ClaimsIdentity ci = HttpContext.User.Identities.FirstOrDefault();
            string userName = "";
            string userType = "";
            if (ci.FindFirst("admin") != null)
            {
                userName = ci.FindFirst("admin").Value;
                userType = "admin";
            }
            else if (ci.FindFirst("vet") != null)
            {
                userName = ci.FindFirst("vet").Value;
                userType = "vet";
            }
            else if (ci.FindFirst("volunteer") != null)
            {
                userName = ci.FindFirst("volunteer").Value;
                userType = "volunteer";
            }
                
                return Ok(new UserLoginOutDto
                {
                    UserName = userName,
                    UserType = userType
                });

        }

    }
}
