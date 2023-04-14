using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace SPCA_backend.Model
{
    public class Request
    {
        [Key]
        [Required]
        public int Id { get; set; }
        [Required]
        public int DogId { get; set; }
        [Required]
        public int ScaleId { get; set; }
        [Required]
        public double DogWeight { get; set; }
        [Required]
        public string TimeStamp { get; set; }
       
    }
}
