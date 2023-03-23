using System.ComponentModel.DataAnnotations;

namespace SPCA_backend.Model
{
    public class UserLogin
    {
        [Key]
        [Required]
        public int Id { get; set; }
        [Required]
        public string UserName { get; set; }
        [Required]
        public string PasswordSha256Hash { get; set; }
        [Required]
        public string UserType { get; set; }
    }
}
