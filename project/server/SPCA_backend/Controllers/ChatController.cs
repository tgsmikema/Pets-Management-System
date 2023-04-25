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
    [Route("chat")]
    [ApiController]
    public class ChatController : Controller
    {
        private readonly ISPCARepo _repository;

        public ChatController(ISPCARepo repository)
        {
            _repository = repository;
        }

        [HttpPost("send")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public async Task<ActionResult> sendMessageAsync(MessageInDto messageInDto)
        {
            await _repository.AddNewMessage(messageInDto);

            return Ok("Message Sent.");
        }

        [HttpGet("getAlreadyMessagedPeopleList")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult<IEnumerable<UserOutDto>> getAlreadyMessagedPeopleList(int currentUserId)
        {
            IEnumerable<UserOutDto> allPeople = _repository.getAlreadyMessagedPeopleList(currentUserId);
            return Ok(allPeople);
        }

        [HttpGet("getNeverMessagedPeopleList")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult<IEnumerable<UserOutDto>> getNeverMessagedPeopleList(int currentUserId)
        {
            IEnumerable<UserOutDto> allPeople = _repository.getNeverMessagedPeopleList(currentUserId);
            return Ok(allPeople);
        }

        [HttpGet("getChatHistory")]
        [Authorize(AuthenticationSchemes = "Authentication")]
        [Authorize(Policy = "AllUser")]
        public ActionResult<IEnumerable<MessageOutDto>> getChatHistory(int currentUserId, int chatToUserId)
        {
            IEnumerable<MessageOutDto> allRelatedMessage = _repository.getChatHistory(currentUserId, chatToUserId);
            return Ok(allRelatedMessage);
        }

    }
}
