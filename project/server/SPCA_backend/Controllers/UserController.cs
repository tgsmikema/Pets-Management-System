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
    public class UserController : Controller
    {
        private static bool USER_NAME = true;
        private static bool TOKEN = false;

       private readonly ISPCARepo _repository;

       public UserController(ISPCARepo repository)
        {
            _repository = repository;
        }




        [HttpGet("testing")]
        public ActionResult demoFunction()
        {
            return Ok("This is a 770 Team 2 Hosted backend API");
        }
        



        [HttpPost("register")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        public ActionResult userRegister(UserInDto userLoginInDto)
        {
            bool isRegisterSuccessful = _repository.AddNewUser(userLoginInDto);
            return (isRegisterSuccessful ? Ok("User successfully registered.") : NotFound("Username not available. Please Try again."));
        }




        [HttpPost("register2")]
        public ActionResult userRegister2(UserInDto userLoginInDto)
        {
            bool isRegisterSuccessful = _repository.AddNewUser(userLoginInDto);
            return (isRegisterSuccessful ? Ok("User successfully registered.") : NotFound("Username not available. Please Try again."));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpGet("login")]
        public ActionResult<UserOutDto> userLogin()
        {
            UserOutDto userOutDto = _repository.GetUserInfo(getLoggedInUserUserNameOrToken(USER_NAME));
            userOutDto.Token = getLoggedInUserUserNameOrToken(TOKEN);
            return Ok(userOutDto);

        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AdminOnly")]
        [HttpDelete("delete")]
        public ActionResult userDelete(int userId)
        {
            bool valid = _repository.DeleteUser(userId);
            return (valid ? Ok() : NotFound("User ID does not exist"));
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
        [HttpPost("editUserAccess")]
        public ActionResult editUserAccessLevel(UserAccessInDto userAccessInDto)
        {
            bool valid = _repository.EditExistingUserAccessLevel(userAccessInDto);
            return (valid ? Ok("Successfully changed the selected user access level") : NotFound("An Error Occured, please try again!"));
        }




        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        [HttpPost("changePassword")]
        public ActionResult changePasswordForCurrentUser(UserChangePasswordInDto userChangePasswordInDto)
        {
            int userId = _repository.GetUserIdFromUserName(getLoggedInUserUserNameOrToken(USER_NAME));
            bool isValid = _repository.ChangePasswordForCurrentUser(userId, userChangePasswordInDto);
            string username = getLoggedInUserUserNameOrToken(USER_NAME);
            string newTokenPlainText = username + ":" + userChangePasswordInDto.NewPassword;
            return (isValid ? Ok(Base64Encode(newTokenPlainText)) : NotFound("An Error Occured, please try again!"));
        }





        //---------------------------------------------------------Helper Methods-------------------------------------------------------------





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

        private string retrieveTokenOfLoggedInUser()
        {
            ClaimsIdentity ci = HttpContext.User.Identities.FirstOrDefault();
            string token = "";
            if (ci.FindFirst("admin") != null)
            {
                token = ci.FindFirst("admin").Value;
            }
            else if (ci.FindFirst("vet") != null)
            {
                token = ci.FindFirst("vet").Value;
            }
            else if (ci.FindFirst("volunteer") != null)
            {
                token = ci.FindFirst("volunteer").Value;
            }

            return token;
        }

        private string getLoggedInUserUserNameOrToken(bool isUserName)
        {
            if (isUserName)
            {
                return retrieveUserNameOfLoggedInUser();
            }
            else
            {
                return retrieveTokenOfLoggedInUser();
            }
        }

        private string Base64Encode(string plainText)
        {
            var plainTextBytes = System.Text.Encoding.UTF8.GetBytes(plainText);
            return System.Convert.ToBase64String(plainTextBytes);
        }

    }
}
