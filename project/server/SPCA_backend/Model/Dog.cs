using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace SPCA_backend.Model
{
    public class Dog
    {
        [Key]
        [Required]
        public int Id { get; set; }
        [Required]
        public int CentreId { get; set; }
        [Required]
        public string Name { get; set; }
        [Required]
        public string Breed { get; set; }
        [Required]
        public bool isFlag { get; set; }
        [Required]
        public bool isAlert { get; set; }

    }
}
