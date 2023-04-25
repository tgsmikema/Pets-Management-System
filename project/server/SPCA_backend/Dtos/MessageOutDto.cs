using Microsoft.Identity.Client;
using System.ComponentModel.DataAnnotations;

namespace SPCA_backend.Dtos
{
    public class MessageOutDto
    {
        public int FromUserId { get; set; }
        public int ToUserId { get; set; }
        public string Timestamp { get; set; }
        public string MessageContent { get; set; }

    }
}
