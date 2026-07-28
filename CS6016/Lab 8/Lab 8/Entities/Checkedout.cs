using System;
using System.Collections.Generic;

namespace Lab_8.Entities;

public partial class Checkedout
{
    public int Cardnum { get; set; }

    public int Serial { get; set; }

    public virtual Patron CardnumNavigation { get; set; } = null!;

    public virtual Inventory SerialNavigation { get; set; } = null!;
}
