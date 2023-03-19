using System.ComponentModel.DataAnnotations;

namespace SPCA_backend.Model
{
    public class UserLogin
    {
        [Key]
        public int Id { get; set; }
        [Required]
        public string UserName { get; set; }
        [Required]
        public string Password { get; set; }
        [Required]
        public string UserType { get; set; }
    }
}
