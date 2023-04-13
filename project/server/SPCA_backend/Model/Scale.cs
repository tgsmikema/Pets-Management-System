using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace SPCA_backend.Model
{
    public class Scale
    {
        [Key]
        [Required]
        public int Id { get; set; }
        [Required]
        public int CentreId { get; set; }
        [Required]
        public string Name { get; set; }
    
    }
}
