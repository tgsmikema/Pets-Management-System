using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace SPCA_backend.Model
{
    public class Message
    {
        [Key]
        [Required]
        public int Id { get; set; }
        [Required]
        public string TimeStamp { get; set; }
        [Required]
        public int ToUserId { get; set; }
        [Required]
        public int FromUserId { get; set; }
        [Required]
        public bool IsRead { get; set; }
        [Required]
        public string MessageContent { get; set; }

    }
}
