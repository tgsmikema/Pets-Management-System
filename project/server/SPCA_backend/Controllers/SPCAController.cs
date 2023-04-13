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
    [Route("user")]
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

        [HttpGet("testing2")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult demoFunction2()
        {
            return Ok("Admin function!!!");
        }


        [HttpPost("register")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult userRegister(UserInDto userLoginInDto)
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


        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("login")]
        public ActionResult<UserOutDto> userLogin()
        {
            ClaimsIdentity ci = HttpContext.User.Identities.FirstOrDefault();
            string userName = "";
            string userType = "";
            string token = "";
            if (ci.FindFirst("admin") != null)
            {
                token = ci.FindFirst("admin").Value;
                userName = getUserNameFromHeader(token);
            }
            else if (ci.FindFirst("vet") != null)
            {
                token = ci.FindFirst("vet").Value;
                userName = getUserNameFromHeader(token);
            }
            else if (ci.FindFirst("volunteer") != null)
            {
                token = ci.FindFirst("volunteer").Value;
                userName = getUserNameFromHeader(token);
            }
            UserOutDto userOutDto = _repository.GetUserInfo(userName);
            userOutDto.Token = token;

            return Ok(userOutDto);

        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpGet("delete")]
        public ActionResult userDelete(int userId)
        {
            bool valid = _repository.DeleteUser(userId);
            if (valid)
            {
                return Ok();
            }
            else
            {
                return NotFound("User ID does not exist");
            }
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpGet("getAllUsers")]
        public ActionResult<IEnumerable<UserOutDto>> getAllUsers()
        {
            return Ok(_repository.GetAllUsers());
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpPost("editUserAccessLevel")]

        public ActionResult editUserAccessLevel(UserAccessInDto userAccessInDto)
        {
            bool valid = _repository.EditExistingUserAccessLevel(userAccessInDto);
            if (valid)
            {
                return Ok("Successfully changed the selected user access level");
            }
            else
            {
                return NotFound("An Error Occured, please try again!");
            }
        }

        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpPost("changePassword")]
        public ActionResult changePasswordForCurrentUser(UserChangePasswordInDto userChangePasswordInDto)
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
            int userId = _repository.getUserIdFromUserName(userName);

            bool isValid = _repository.ChangePasswordForCurrentUser(userId, userChangePasswordInDto);

            if (isValid)
            {
                return Ok("Successfully changed the password for the current user.");
            }
            else
            {
                return NotFound("An Error Occured, please try again!");
            }

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
