using System.ComponentModel.DataAnnotations;

namespace SPCA_backend.Model
{
    public class Customer
    {
        [Key]
        public int Id { get; set; }
        [Required]
        public string FirstName { get; set; }
        [Required]
        public string LastName { get; set; }
        public string? Email { get; set; }
        public string? Address { get; set; }
    }
}
