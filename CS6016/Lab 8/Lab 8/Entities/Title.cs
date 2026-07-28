using System;
using System.Collections.Generic;

namespace Lab_8.Entities;

public partial class Title
{
    public string Isbn { get; set; } = null!;

    public string Title1 { get; set; } = null!;

    public string Author { get; set; } = null!;

    public virtual ICollection<Inventory> Inventories { get; set; } = new List<Inventory>();
}
