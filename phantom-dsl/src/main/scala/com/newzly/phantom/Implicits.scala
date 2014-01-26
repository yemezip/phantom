/*
 * Copyright 2013 newzly ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com
package newzly

package phantom

import com.newzly.phantom.column.AbstractColumn
import com.newzly.phantom.keys.LongOrderKey
import com.newzly.phantom.query.{ SelectQuery, SelectWhere }

object Implicits {

  type Column[Owner <: CassandraTable[Owner, Record], Record, T] = com.newzly.phantom.column.Column[Owner, Record, T]
  type PrimitiveColumn[Owner <: CassandraTable[Owner, Record], Record, T] =  com.newzly.phantom.column.PrimitiveColumn[Owner, Record, T]
  type OptionalColumn[Owner <: CassandraTable[Owner, Record], Record, T] =  com.newzly.phantom.column.OptionalColumn[Owner, Record, T]
  type OptionalPrimitiveColumn[Owner <: CassandraTable[Owner, Record], Record, T] =  com.newzly.phantom.column.OptionalPrimitiveColumn[Owner, Record, T]
  type JsonColumn[Owner <: CassandraTable[Owner, Record], Record, T] =  com.newzly.phantom.column.JsonColumn[Owner, Record, T]
  type JsonSeqColumn[Owner <: CassandraTable[Owner, Record], Record, T] =  com.newzly.phantom.column.JsonSeqColumn[Owner, Record, T]
  type ListColumn[Owner <: CassandraTable[Owner, Record], Record, T] = com.newzly.phantom.column.ListColumn[Owner, Record, T]
  type SetColumn[Owner <: CassandraTable[Owner, Record], Record, T] =  com.newzly.phantom.column.SetColumn[Owner, Record, T]
  type SeqColumn[Owner <: CassandraTable[Owner, Record], Record, T] =  com.newzly.phantom.column.SeqColumn[Owner, Record, T]
  type MapColumn[Owner <: CassandraTable[Owner, Record], Record, K, V] =  com.newzly.phantom.column.MapColumn[Owner, Record, K, V]

  implicit def columnToQueryColumn[Owner <: CassandraTable[Owner, Record], Record, RR: CassandraPrimitive](col: Column[Owner, Record, RR]) =
    new QueryColumn(col)

  implicit def simpleColumnToAssignment[Owner <: CassandraTable[Owner, Record], Record, RR: CassandraPrimitive](col: AbstractColumn[RR]) = {
    new ModifyColumn[RR](col)
  }

  implicit def simpleOptionalColumnToAssignment[Owner <: CassandraTable[Owner, Record], Record, RR: CassandraPrimitive](col: OptionalColumn[Owner, Record, RR]) = {
    new ModifyColumnOptional[Owner, Record, RR](col)
  }

  implicit def jsonColumnToAssignment[Owner <: CassandraTable[Owner, Record], Record, RR: Manifest](col: JsonColumn[Owner, Record, RR]) = {
    new ModifyColumn[RR](col)
  }

  implicit def listColumnToAssignment[Owner <: CassandraTable[Owner, Record], Record, RR: CassandraPrimitive](col: ListColumn[Owner, Record, RR]) = {
    new ModifyColumn[List[RR]](col)
  }

  implicit def seqColumnToAssignment[Owner <: CassandraTable[Owner, Record], Record, RR: CassandraPrimitive](col: SeqColumn[Owner, Record, RR]) = {
    new ModifyColumn[Seq[RR]](col)
  }

  implicit def jsonSeqColumnToAssignment[Owner <: CassandraTable[Owner, Record], Record, RR: Manifest](col: JsonSeqColumn[Owner, Record, RR]) = {
    new ModifyColumn[Seq[RR]](col)
  }

  implicit def columnIsSeleCassandraTable[Owner <: CassandraTable[Owner, Record], Record, T](col: Column[Owner, Record, T]): SelectColumn[T] =
    new SelectColumnRequired[Owner, Record, T](col)

  implicit def optionalColumnIsSeleCassandraTable[Owner <: CassandraTable[Owner, Record], Record, T](col: OptionalColumn[Owner, Record, T]): SelectColumn[Option[T]] =
    new SelectColumnOptional[Owner, Record, T](col)

  implicit class SkipSelect[T <: CassandraTable[T, R] with LongOrderKey[T, R], R](val select: SelectQuery[T, R]) extends AnyVal {
    def skip(l: Int): SelectWhere[T, R] = {
      select.where(_.order_id gt l.toLong)
    }

    def skip(l: Long): SelectWhere[T, R] = {
      select.where(_.order_id gt l)
    }
  }
}