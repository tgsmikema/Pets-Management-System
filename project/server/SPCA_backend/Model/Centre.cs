using System.ComponentModel.DataAnnotations;

namespace SPCA_backend.Model
{
    public class Centre
    {
        [Key]
        [Required]
        public int Id { get; set; }
        [Required]
        public string Name { get; set; }
    
    }
}
